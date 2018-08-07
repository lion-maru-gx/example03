#example03(MIDI Filer)
##概要
本プログラムはJavaFXとMIDIの練習のために作成しました。
example01をもとに作成されています。
以下の機能を実現しています。
・MIDIデータの録音しSMF形式でファイルに書き出す。
・SMF形式のファイルを読込み再生する。

##MIDIデバイス
MIDIデバイスはMidiSystem.getMidiDeviceInfo()でデバイス情報を取得することができる。
MidiDeviceがハードウェアMIDIポートを表しているかどうかを判断するには以下の方法を用いる。

 MidiDevice device = ...;
 if ( ! (device instanceof Sequencer) && ! (device instanceof Synthesizer)) {
   // we're now sure that device represents a MIDI port
   // ...
 }

また入力デバイス／出力デバイスの判定はTransmitter、Receiverの有無で判定する。
それぞれのデバイス情報は以下のように取得する。
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
				// getMaxTransmitters()が0以上の場合は入力デバイス
				// Synthesizer、Sequencer以外は
				if (device.getMaxTransmitters() > 0 && !(device instanceof Synthesizer)
						&& !(device instanceof Sequencer))
					list.add(infos[i]);
			} catch (MidiUnavailableException e) {
				e.printStackTrace();
			}
		}
		return list.toArray(new MidiDevice.Info[0]);
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

##MIDI入力
MIDI入力はReceiverインターフェースから作成し、send()メソッドを実装する。
example01ではMidiMessageだけを取得していたが、今回は時間情報も必要となるのでMidiEventでQueueを作成する。
また不要なメッセージを取得しないようにそれぞれのメッセージグループ毎に有効／無効の設定ができるようにする。

##MIDI出力

##SMF
##参照文献
・MIDI1.0規格書 - 音楽電子事業協会
http://amei.or.jp/midistandardcommittee/MIDIspcj.html


