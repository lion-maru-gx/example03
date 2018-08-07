package jp.gr.java_conf.lion_maru_gx.example.example03;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.fxml.FXML;
import jp.gr.java_conf.lion_maru_gx.example.common.MidiUtil;

public class MainController {
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	/**
	 * 終了ボタン処理
	 */
	@FXML
	public void handleExit() {
		MidiUtil.close();
		Platform.exit();
	}

}
