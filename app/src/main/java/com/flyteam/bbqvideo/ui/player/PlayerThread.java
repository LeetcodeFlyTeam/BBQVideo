package com.flyteam.bbqvideo.ui.player;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.nio.ByteBuffer;

public class PlayerThread extends Thread {
    private static final String VIDEO = "video/";
    private Surface surface;
    private MediaExtractor mExtractor;
    private MediaCodec decoder;

    public boolean init(@NonNull Surface surface, @NonNull String source) {
        this.surface = surface;
        try {
            mExtractor = new MediaExtractor();
            mExtractor.setDataSource(source);
            int trackCount = mExtractor.getTrackCount();
            for (int i = 0; i < trackCount; i++) {
                MediaFormat format = mExtractor.getTrackFormat(i);
                String mime = format.getString(MediaFormat.KEY_MIME);
                if (mime != null && mime.startsWith(VIDEO)) {
                    mExtractor.selectTrack(i);
                    decoder = MediaCodec.createDecoderByType(mime);
                    decoder.configure(format, surface, null, 0);
                }
            }
            if (decoder != null) {
                decoder.start();
            }
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void run() {
        try {
            ByteBuffer[] inputBuffers = decoder.getInputBuffers();
            ByteBuffer[] outputBuffers = decoder.getOutputBuffers();
            MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
            boolean isEOS = false;
            long startMs = System.currentTimeMillis();

            while (!Thread.interrupted()) {
                if (!isEOS) {
                    int inIndex = decoder.dequeueInputBuffer(10000);
                    if (inIndex >= 0) {
                        ByteBuffer buffer = inputBuffers[inIndex];
                        int sampleSize = mExtractor.readSampleData(buffer, 0);
                        if (sampleSize < 0) {
                            // We shouldn't stop the playback at this point, just pass the EOS
                            // flag to decoder, we will get it again from the
                            // dequeueOutputBuffer
                            Log.d("DecodeActivity", "InputBuffer BUFFER_FLAG_END_OF_STREAM");
                            decoder.queueInputBuffer(inIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                            isEOS = true;
                        } else {
                            decoder.queueInputBuffer(inIndex, 0, sampleSize, mExtractor.getSampleTime(), 0);
                            mExtractor.advance();
                        }
                    }
                }

                int outIndex = decoder.dequeueOutputBuffer(info, 10000);
                switch (outIndex) {
                    case MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED:
                        Log.d("DecodeActivity", "INFO_OUTPUT_BUFFERS_CHANGED");
                        outputBuffers = decoder.getOutputBuffers();
                        break;
                    case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:
                        Log.d("DecodeActivity", "New format " + decoder.getOutputFormat());
                        break;
                    case MediaCodec.INFO_TRY_AGAIN_LATER:
                        Log.d("DecodeActivity", "dequeueOutputBuffer timed out!");
                        break;
                    default:
                        ByteBuffer buffer = outputBuffers[outIndex];
                        Log.v("DecodeActivity", "We can't use this buffer but render it due to the API limit, " + buffer);

                        // We use a very simple clock to keep the video FPS, or the video
                        // playback will be too fast
                        while (info.presentationTimeUs / 1000 > System.currentTimeMillis() - startMs) {
                            try {
                                sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                break;
                            }
                        }
                        decoder.releaseOutputBuffer(outIndex, true);
                        break;
                }

                // All decoded frames have been rendered, we can stop playing now
                if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                    Log.d("DecodeActivity", "OutputBuffer BUFFER_FLAG_END_OF_STREAM");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            decoder.stop();
            decoder.release();
            mExtractor.release();
        }
    }
}
