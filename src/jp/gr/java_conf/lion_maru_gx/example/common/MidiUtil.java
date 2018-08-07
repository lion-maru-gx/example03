package jp.gr.java_conf.lion_maru_gx.example.common;

import java.util.ArrayList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
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
	private static MidiInputQueue receiver;

	/**
	 * staticの初期化
	 */
	static {

		// MIDI入力用レシーバの定義
		receiver = new MidiInputQueue();

	}

	/**
	 * 出力デバイス情報リスト取得
	 *
	 * @return
	 * @throws MidiUnavailableException
	 */
	public static MidiDevice.Info[] getOutputMidiDeviceInfo() throws MidiUnavailableException {
		ArrayList<MidiDevice.Info> list = new ArrayList<>();

		MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
		for (int i = 0; i < infos.length; i++) {
			// throws MidiUnavailableException
			MidiDevice device = MidiSystem.getMidiDevice(infos[i]);
			// getMaxReceivers()が0以上の場合は出力デバイス
			// Synthesizer、Sequencer以外はハードウェアMIDIポート
			if (device.getMaxReceivers() > 0 && !(device instanceof Synthesizer) && !(device instanceof Sequencer))
				list.add(infos[i]);
		}
		return list.toArray(new MidiDevice.Info[0]);
	}

	/**
	 * 入力デバイス情報リストの取得
	 *
	 * @return デバイス情報リスト
	 * @throws MidiUnavailableException
	 */
	public static MidiDevice.Info[] getInputMidiDeviceInfo() throws MidiUnavailableException {
		ArrayList<MidiDevice.Info> list = new ArrayList<>();

		MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
		for (int i = 0; i < infos.length; i++) {
			// throws MidiUnavailableException
			MidiDevice device = MidiSystem.getMidiDevice(infos[i]);
			// getMaxTransmitters()が0以上の場合は入力デバイス
			// Synthesizer、Sequencer以外はハードウェアMIDIポート
			if (device.getMaxTransmitters() > 0 && !(device instanceof Synthesizer)
					&& !(device instanceof Sequencer))
				list.add(infos[i]);
		}
		return list.toArray(new MidiDevice.Info[0]);
	}

	/**
	 * 出力デバイス名リストの取得
	 *
	 * @return デバイス名リスト
	 * @throws MidiUnavailableException
	 */
	public static String[] getOutputNames() throws MidiUnavailableException {
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
	 * @throws MidiUnavailableException
	 */
	public static String[] getInputNames() throws MidiUnavailableException {
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
	 * @throws MidiUnavailableException
	 */
	public static MidiDevice getInputMidiDevice(String deviceName) throws MidiUnavailableException {
		for (MidiDevice.Info info : getInputMidiDeviceInfo()) {
			if (info.getName().equals(deviceName)) {
				return MidiSystem.getMidiDevice(info);
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
	 * @throws MidiUnavailableException
	 */
	public static MidiDevice getOutputMidiDevice(String deviceName) throws MidiUnavailableException {
		for (MidiDevice.Info info : getOutputMidiDeviceInfo()) {
			if (info.getName().equals(deviceName)) {
				return MidiSystem.getMidiDevice(info);
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
	 * @throws MidiUnavailableException
	 */
	public static void setInputDevice(MidiDevice midiDevice) throws MidiUnavailableException {
		inputDevice = midiDevice;
		if (!inputDevice.isOpen()) {
			inputDevice.open();
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
	 * @throws MidiUnavailableException
	 */
	public static void setInputDeviceName(String DeviceName) throws MidiUnavailableException {
		inputDevice = getInputMidiDevice(DeviceName);
		if (!inputDevice.isOpen()) {
			inputDevice.open();
		}
		inputDevice.getTransmitter().setReceiver(receiver);
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
	 * @throws MidiUnavailableException
	 */
	public static void setOutputDevice(MidiDevice midiDevice) throws MidiUnavailableException {
		outputDevice = midiDevice;
		if (!outputDevice.isOpen()) {
			outputDevice.open();
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
	 * @throws MidiUnavailableException
	 */
	public static void setOutputDeviceName(String deviceName) throws MidiUnavailableException {
		if (outputDevice != null && outputDevice.isOpen()) {
			outputDevice.close();
		}
		outputDevice = getOutputMidiDevice(deviceName);
		if (!outputDevice.isOpen()) {
			outputDevice.open();
		}
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
	 * @throws InvalidMidiDataException
	 * @throws MidiUnavailableException
	 */
	public static void sendMessage(String message)
			throws IllegalArgumentException, MidiUnavailableException, InvalidMidiDataException {
		byte[] buff = DatatypeConverter.parseHexBinary(message);
		sendMessage(buff);
	}

	/**
	 * メッセージ送信
	 *
	 * @param message
	 * @throws MidiUnavailableException
	 * @throws InvalidMidiDataException
	 */
	public static void sendMessage(byte[] message) throws MidiUnavailableException, InvalidMidiDataException {
		SysexMessage msg = new SysexMessage();
		msg.setMessage(message, message.length);
		getOutputDevice().getReceiver().send(msg, 0);
	}

}
