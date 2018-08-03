package jp.gr.java_conf.lion_maru_gx.example.example03;

import javax.sound.midi.Patch;

public class FB01VoicePatch extends Patch {
	private int param1=1;

	public FB01VoicePatch(int arg0, int arg1) {
		super(arg0, arg1);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public int getParam1() {
		return param1;
	}


	public void setParam1(int param1) {
		this.param1 = param1;
	}


}
