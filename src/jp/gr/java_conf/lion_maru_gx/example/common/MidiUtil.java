package jp.gr.java_conf.lion_maru_gx.example.common;

import java.util.ArrayList;
import java.util.LinkedList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.SysexMessage;
import javax.xml.bind.DatatypeConverter;

/**
 * Midiデバイスの取得
 *
 * @author lion-maru-gx
 *
 */
public class MidiUtil {
	/**
	 * 入力デバイス
	 */
	private static MidiDevice inputDevice = null;
	/**
	 * 出力デバイス
	 */
	private static MidiDevice outputDevice = null;
	/**
	 * 入力レシーバ
	 */
	private static Receiver receiver;
	/**
	 * 入力メッセージキュー
	 */
	private static LinkedList<MidiEvent> inputMidiEventQueue = new LinkedList<>();
	/**
	 * ShortMessageの有効／無効
	 */
	private static boolean shortMessageActive = true;

	/**
	 * SysexMessageの有効／無効
	 */
	private static boolean sysexMessageActive = true;

	/**
	 * SysexMessageの有効／無効
	 */
	private static boolean systemMessageActive = false;

	public static boolean isShortMessageActive() {
		return shortMessageActive;
	}

	public static void setShortMessageActive(boolean inShortMessageActive) {
		shortMessageActive = inShortMessageActive;
	}

	public static boolean isSysexMessageActive() {
		return sysexMessageActive;
	}

	public static void setSysexMessageActive(boolean inSysexMessageActive) {
		sysexMessageActive = inSysexMessageActive;
	}

	public static boolean isSystemMessageActive() {
		return systemMessageActive;
	}

	public static void setSystemMessageActive(boolean inSystemMessageActive) {
		systemMessageActive = inSystemMessageActive;
	}

	/**
	 * staticの初期化
	 */
	static {

		// MIDI入力用レシーバの定義
		receiver = new Receiver() {
			long baseTime=0;

			@Override
			public void close() {
				inputMidiEventQueue.clear();
			}

			@Override
			public void send(MidiMessage message, long timeStamp) {
				if (message != null) {
					// レシーバがタイムスタンプをサポートしていない場合は、
					// タイムスタンプ値は -1になるためシステム時間を使用する。
					if(timeStamp == -1) {
						timeStamp = System.currentTimeMillis();
					}
					if(baseTime == 0) {
						baseTime = timeStamp;
					}
					inputMidiEventQueue.offer(new MidiEvent(message,timeStamp - baseTime));
					baseTime = timeStamp;
				}
			}

		};

	}

	/**
	 * 出力デバイス情報リスト取得
	 *
	 * @return
	 */
	public static MidiDevice.Info[] getOutputMidiDeviceInfo() {
		ArrayList<MidiDevice.Info> list = new ArrayList<>();

		MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
		for (int i = 0; i < infos.length; i++) {
			// throws MidiUnavailableException
			try {
				MidiDevice device = MidiSystem.getMidiDevice(infos[i]);
				if (device.getMaxReceivers() != 0 && !(device instanceof Synthesizer) && !(device instanceof Sequencer))
					list.add(infos[i]);
			} catch (MidiUnavailableException e) {
				e.printStackTrace();
			}
		}
		return list.toArray(new MidiDevice.Info[0]);
	}

	/**
	 * 入力デバイス情報リストの取得
	 *
	 * @return デバイス情報リスト
	 */
	public static MidiDevice.Info[] getInputMidiDeviceInfo() {
		ArrayList<MidiDevice.Info> list = new ArrayList<>();

		MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
		for (int i = 0; i < infos.length; i++) {
			// throws MidiUnavailableException
			try {
				MidiDevice device = MidiSystem.getMidiDevice(infos[i]);
				if (device.getMaxTransmitters() != 0 && !(device instanceof Synthesizer)
						&& !(device instanceof Sequencer))
					list.add(infos[i]);
			} catch (MidiUnavailableException e) {
				e.printStackTrace();
			}
		}
		return list.toArray(new MidiDevice.Info[0]);
	}

	/**
	 * 出力デバイス名リストの取得
	 *
	 * @return デバイス名リスト
	 */
	public static String[] getOutputNames() {
		MidiDevice.Info[] outputMidiDeviceInfo = getOutputMidiDeviceInfo();
		String[] outputNames = new String[outputMidiDeviceInfo.length];
		for (int i = 0; i < outputMidiDeviceInfo.length; i++) {
			outputNames[i] = outputMidiDeviceInfo[i].getName();
		}
		return outputNames;
	}

	/**
	 * 入力デバイス名リストの取得
	 *
	 * @return デバイス名リスト
	 */
	public static String[] getInputNames() {
		MidiDevice.Info[] inputMidiDeviceInfo = getInputMidiDeviceInfo();
		String[] inputNames = new String[inputMidiDeviceInfo.length];
		for (int i = 0; i < inputMidiDeviceInfo.length; i++) {
			inputNames[i] = inputMidiDeviceInfo[i].getName();
		}
		return inputNames;
	}

	/**
	 * 入力デバイスの取得
	 *
	 * @param deviceName
	 *            デバイス名
	 * @return デバイス名リスト
	 */
	public static MidiDevice getInputMidiDevice(String deviceName) {
		for (MidiDevice.Info info : getInputMidiDeviceInfo()) {
			if (info.getName().equals(deviceName)) {
				try {
					return MidiSystem.getMidiDevice(info);
				} catch (MidiUnavailableException e) {
					e.printStackTrace();
					return null;
				}
			}
		}
		return null;

	}

	/**
	 * 出力デバイスの取得
	 *
	 * @param deviceName
	 *            デバイス名
	 * @return デバイス
	 */
	public static MidiDevice getOutputMidiDevice(String deviceName) {
		for (MidiDevice.Info info : getOutputMidiDeviceInfo()) {
			if (info.getName().equals(deviceName)) {
				try {
					return MidiSystem.getMidiDevice(info);
				} catch (MidiUnavailableException e) {
					e.printStackTrace();
					return null;
				}
			}
		}
		return null;

	}

	/**
	 * 入力デバイスの取得
	 *
	 * @return デバイス
	 */
	public static MidiDevice getInputDevice() {
		return inputDevice;
	}

	/**
	 * 入力デバイスの設定
	 *
	 * @param midiDevice
	 */
	public static void setInputDevice(MidiDevice midiDevice) {
		inputDevice = midiDevice;
		if (!inputDevice.isOpen()) {
			try {
				inputDevice.open();
			} catch (MidiUnavailableException e1) {
				e1.printStackTrace();
			}
		}
		try {
			inputDevice.getTransmitter().setReceiver(receiver);
		} catch (MidiUnavailableException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 入力デバイス名の取得
	 *
	 * @return
	 */
	public static String getInputDeviceName() {
		if (inputDevice != null) {
			return inputDevice.getDeviceInfo().getName();
		}
		return "";
	}

	/**
	 * 入力デバイス名の設定
	 *
	 * @param DeviceName
	 */
	public static void setInputDeviceName(String DeviceName) {
		inputDevice = getInputMidiDevice(DeviceName);
		if (!inputDevice.isOpen()) {
			try {
				inputDevice.open();
			} catch (MidiUnavailableException e1) {
				e1.printStackTrace();
			}
		}
		try {
			inputDevice.getTransmitter().setReceiver(receiver);
		} catch (MidiUnavailableException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 出力デバイスの取得
	 *
	 * @return
	 */
	public static MidiDevice getOutputDevice() {
		return outputDevice;
	}

	/**
	 * 出力デバイスの設定
	 *
	 * @param midiDevice
	 */
	public static void setOutputDevice(MidiDevice midiDevice) {
		outputDevice = midiDevice;
		if (!outputDevice.isOpen()) {
			try {
				outputDevice.open();
			} catch (MidiUnavailableException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 出力デバイス名の取得
	 *
	 * @return
	 */
	public static String getOutputDeviceName() {
		if (outputDevice != null) {
			return outputDevice.getDeviceInfo().getName();
		}
		return "";
	}

	/**
	 * 出力デバイス名の設定
	 *
	 * @param deviceName
	 */
	public static void setOutputDeviceName(String deviceName) {
		if (outputDevice != null && outputDevice.isOpen()) {
			outputDevice.close();
		}
		outputDevice = getOutputMidiDevice(deviceName);
		if (!outputDevice.isOpen()) {
			try {
				outputDevice.open();
			} catch (MidiUnavailableException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 入力MIDIイベントの取得
	 */
	public static MidiEvent getInputMidiEvent() {
		if(inputMidiEventQueue.isEmpty()) {
			return null;
		}
		return inputMidiEventQueue.poll();
	}
	/**
	 * 入力メッセージの取得
	 *
	 * @return
	 */
	public static MidiMessage getInputMessage() {
		MidiMessage message = null;
		while (!inputMidiEventQueue.isEmpty()) {
			message = getInputMidiEvent().getMessage();
			if (message.getMessage()[0] == (byte) 0xf8 ||
					message.getMessage()[0] == (byte) 0xfe) {
				if(isSystemMessageActive()) {
					break;
				}
				message = null;
			}
			if (message instanceof ShortMessage) {
				if(isShortMessageActive()) {
					break;
				}
				message = null;
			} else if (message instanceof SysexMessage) {
				if(isSysexMessageActive()) {
					break;
				}
				message = null;
			}
		}
		return message;
	}

	/**
	 * 入力メッセージの取得
	 *
	 * @return
	 */
	public static String getInputMessages() {
		String msg = "";
		while (!inputMidiEventQueue.isEmpty()) {
			MidiMessage midiMsg = getInputMessage();
			if(midiMsg != null) {
				msg = msg + DatatypeConverter.printHexBinary(midiMsg.getMessage()) + "\n";
			}
		}
		return msg;
	}

	/**
	 * 終了処理
	 */
	public static void close() {
		if (outputDevice != null) {
			if (outputDevice.isOpen()) {
				outputDevice.close();
			}
		}
		if (inputDevice != null) {
			if (inputDevice.isOpen()) {
				inputDevice.close();
			}
		}
	}

	/**
	 * メッセージ送信
	 *
	 * @param message
	 */
	public static void sendMessage(String message) {
		byte[] buff = DatatypeConverter.parseHexBinary(message);
		sendMessage(buff);
	}

	/**
	 * メッセージ送信
	 *
	 * @param message
	 */
	public static void sendMessage(byte[] message) {
		SysexMessage msg = new SysexMessage();
		try {
			msg.setMessage(message, message.length);
			getOutputDevice().getReceiver().send(msg, 0);
		} catch (InvalidMidiDataException | MidiUnavailableException e) {
			e.printStackTrace();
		}
	}

}
