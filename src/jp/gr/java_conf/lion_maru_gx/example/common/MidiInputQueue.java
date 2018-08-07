package jp.gr.java_conf.lion_maru_gx.example.common;

import java.util.LinkedList;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;
import javax.xml.bind.DatatypeConverter;

public class MidiInputQueue implements Receiver {
	/**
	 * 入力メッセージキュー
	 */
	private LinkedList<MidiEvent> inputMidiEventQueue = new LinkedList<>();
	/**
	 * 事前のtimeStamp
	 */
	private long baseTime = 0;

	/**
	 * チャンネル・ボイス・メッセージの有効／無効
	 */
	private boolean chanelVoiceMessageActive = true;
	/**
	 * システム・リアルタイムメッセージの有効／無効
	 */
	private boolean systemRealtimeMessageActive = true;
	/**
	 * システム・コモン・メッセージの有効／無効
	 */
	private boolean systemCommonMessageActive = false;
	/**
	 * SysexMessageの有効／無効
	 */

	private boolean sysexMessageActive = true;

	@Override
	public void send(MidiMessage message, long timeStamp) {
		if (message != null) {
			// 受信メッセージをフィルタリングします。
			if (message instanceof ShortMessage) {
				switch (message.getStatus()) {
				case ShortMessage.NOTE_OFF: // 0x80
				case ShortMessage.NOTE_ON: // 0x90
				case ShortMessage.POLY_PRESSURE: //0xA0
				case ShortMessage.CONTROL_CHANGE://0xB0
				case ShortMessage.PROGRAM_CHANGE://0xC0
				case ShortMessage.CHANNEL_PRESSURE://0xD0
				case ShortMessage.PITCH_BEND://0xE0
					if (!isChanelVoiceMessageActive()) {
						return;
					}
					break;
				case ShortMessage.MIDI_TIME_CODE://0xF1
				case ShortMessage.SONG_POSITION_POINTER://0xF2
				case ShortMessage.SONG_SELECT://0xF3
				case ShortMessage.TUNE_REQUEST://0xF6
				case ShortMessage.END_OF_EXCLUSIVE://0xF7
					if (!isSystemCommonMessageActive()) {
						return;
					}
					break;
				case ShortMessage.TIMING_CLOCK://0xF8
				case ShortMessage.START://0xFA
				case ShortMessage.CONTINUE://0xFB
				case ShortMessage.STOP://0xFC
				case ShortMessage.ACTIVE_SENSING://0xFE
				case ShortMessage.SYSTEM_RESET://0xFF
					if (!isSystemRealtimeMessageActive()) {
						return;
					}
				}
			} else if (message instanceof SysexMessage) {
				if (!isSysexMessageActive()) {
					return;
				}
			}
			// レシーバがタイムスタンプをサポートしていない場合は、
			// タイムスタンプ値は -1になるためシステム時間を使用します。
			if (timeStamp == -1) {
				timeStamp = System.currentTimeMillis();
			}
			if (baseTime == 0) {
				baseTime = timeStamp;
			}
			inputMidiEventQueue.offer(new MidiEvent(message, timeStamp - baseTime));
			baseTime = timeStamp;
		}
	}

	@Override
	public void close() {
		inputMidiEventQueue.clear();
	}

	/**
	 * 入力MIDIイベントの取得します。
	 */
	public MidiEvent getInputMidiEvent() {
		if (inputMidiEventQueue.isEmpty()) {
			return null;
		}
		return inputMidiEventQueue.poll();
	}

	/**
	 * 入力メッセージの取得します。
	 *
	 * @return
	 */
	public MidiMessage getInputMessage() {
		if (inputMidiEventQueue.isEmpty()) {
			return null;
		}
		return getInputMidiEvent().getMessage();
	}

	/**
	 * 入力メッセージを取得します。
	 *
	 * @return
	 */
	public String getInputMessages() {
		String msg = "";
		while (!inputMidiEventQueue.isEmpty()) {
			MidiMessage midiMsg = getInputMessage();
			if (midiMsg != null) {
				msg = msg + DatatypeConverter.printHexBinary(midiMsg.getMessage()) + "\n";
			}
		}
		return msg;
	}

	/**
	 * チャンネル・ボイス・メッセージの有効／無効を取得します。
	 * @return
	 */
	public boolean isChanelVoiceMessageActive() {
		return chanelVoiceMessageActive;
	}

	/**
	 * チャンネル・ボイス・メッセージの有効／無効を設定します。
	 * @param chanelVoiceMessageActive
	 */
	public void setChanelVoiceMessageActive(boolean chanelVoiceMessageActive) {
		this.chanelVoiceMessageActive = chanelVoiceMessageActive;
	}

	/**
	 * システム・リアルタイムメッセージの有効／無効を取得します。
	 * @return
	 */
	public boolean isSystemRealtimeMessageActive() {
		return systemRealtimeMessageActive;
	}

	/**
	 * システム・リアルタイムメッセージの有効／無効を設定します。
	 * @param systemRealtimeMessageActive
	 */
	public void setSystemRealtimeMessageActive(boolean systemRealtimeMessageActive) {
		this.systemRealtimeMessageActive = systemRealtimeMessageActive;
	}

	/**
	 * システム・コモン・メッセージの有効／無効を取得します。
	 * @return
	 */
	public boolean isSystemCommonMessageActive() {
		return systemCommonMessageActive;
	}

	/**
	 * システム・コモン・メッセージの有効／無効を設定します。
	 * @param systemCommonMessageActive
	 */
	public void setSystemCommonMessageActive(boolean systemCommonMessageActive) {
		this.systemCommonMessageActive = systemCommonMessageActive;
	}

	/**
	 * SysexMessageの有効／無効を取得します。
	 * @return
	 */
	public boolean isSysexMessageActive() {
		return sysexMessageActive;
	}

	/**
	 * SysexMessageの有効／無効を設定します。
	 * @param sysexMessageActive
	 */
	public void setSysexMessageActive(boolean sysexMessageActive) {
		this.sysexMessageActive = sysexMessageActive;
	}

}
