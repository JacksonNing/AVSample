package com.devyk.mediacodec_audio_encode.controller

import android.media.MediaCodec
import com.devyk.mediacodec_audio_encode.AudioConfiguration
import com.devyk.mediacodec_audio_encode.mediacodec.OnAudioEncodeListener
import com.devyk.mediacodec_audio_encode.utils.EncodeCastUtils
import java.nio.ByteBuffer

/**
 * <pre>
 *     author  : devyk on 2020-06-13 16:49
 *     blog    : https://juejin.im/user/578259398ac2470061f3a3fb/posts
 *     github  : https://github.com/yangkun19921001
 *     mailbox : yang1001yk@gmail.com
 *     desc    : This is StreamController
 * </pre>
 */
public class StreamController : OnAudioEncodeListener {

    private var mPacker: IMediaCodecListener? = null
    private lateinit var mAudioController: IAudioController

    constructor(audioProcessor: IAudioController) {
        mAudioController = audioProcessor
    }

    public fun setListener(listener: IMediaCodecListener) {
        mPacker = listener
    }

    fun setAudioConfiguration(audioConfiguration: AudioConfiguration) {
        mAudioController.setAudioConfiguration(audioConfiguration)
    }


    @Synchronized
    fun start() {
        EncodeCastUtils.processNotUI(object : EncodeCastUtils.INotUIProcessor {
            override fun process() {
                mPacker?.start()
                mAudioController.setAudioEncodeListener(this@StreamController)
                mAudioController.start()
            }
        })
    }

    @Synchronized
    fun stop() {
        EncodeCastUtils.processNotUI(object : EncodeCastUtils.INotUIProcessor {
            override fun process() {
                mAudioController.setAudioEncodeListener(null)
                mAudioController.stop()
                mPacker?.stop()
            }
        })
    }

    @Synchronized
    fun pause() {
        EncodeCastUtils.processNotUI(object : EncodeCastUtils.INotUIProcessor {
            override fun process() {
                mAudioController.pause()
            }
        })
    }

    @Synchronized
    fun resume() {
        EncodeCastUtils.processNotUI(object : EncodeCastUtils.INotUIProcessor {
            override fun process() {
                mAudioController.resume()
            }
        })
    }

    fun mute(mute: Boolean) {
        mAudioController.mute(mute)
    }

    fun getSessionId(): Int {
        return mAudioController.sessionId
    }

    override fun onAudioEncode(bb: ByteBuffer, bi: MediaCodec.BufferInfo) {
        mPacker?.onAudioAACData(bb, bi)
    }


}