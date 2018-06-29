package jp.gr.java_conf.lion_maru_gx.example.example03;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class VoiceListController implements Initializable {
	@FXML
	private Button voice_0;
	@FXML
	private Button voice_1;
	@FXML
	private Button voice_2;
	@FXML
	private Button voice_3;
	@FXML
	private Button voice_4;
	@FXML
	private Button voice_5;
	@FXML
	private Button voice_6;
	@FXML
	private Button voice_7;
	@FXML
	private Button voice_8;
	@FXML
	private Button voice_9;
	@FXML
	private Button voice_10;
	@FXML
	private Button voice_11;
	@FXML
	private Button voice_12;
	@FXML
	private Button voice_13;
	@FXML
	private Button voice_14;
	@FXML
	private Button voice_15;
	@FXML
	private Button voice_16;
	@FXML
	private Button voice_17;
	@FXML
	private Button voice_18;
	@FXML
	private Button voice_19;
	@FXML
	private Button voice_20;
	@FXML
	private Button voice_21;
	@FXML
	private Button voice_22;
	@FXML
	private Button voice_23;
	@FXML
	private Button voice_24;
	@FXML
	private Button voice_25;
	@FXML
	private Button voice_26;
	@FXML
	private Button voice_27;
	@FXML
	private Button voice_28;
	@FXML
	private Button voice_29;
	@FXML
	private Button voice_30;
	@FXML
	private Button voice_31;
	@FXML
	private Button voice_32;
	@FXML
	private Button voice_33;
	@FXML
	private Button voice_34;
	@FXML
	private Button voice_35;
	@FXML
	private Button voice_36;
	@FXML
	private Button voice_37;
	@FXML
	private Button voice_38;
	@FXML
	private Button voice_39;
	@FXML
	private Button voice_40;
	@FXML
	private Button voice_41;
	@FXML
	private Button voice_42;
	@FXML
	private Button voice_43;
	@FXML
	private Button voice_44;
	@FXML
	private Button voice_45;
	@FXML
	private Button voice_46;
	@FXML
	private Button voice_47;

	private Button voice[];

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		voice = new Button[] {
				voice_0, voice_1, voice_2, voice_3, voice_4, voice_5, voice_6, voice_7, voice_8, voice_9,
				voice_10, voice_11, voice_12, voice_13, voice_14, voice_15, voice_16, voice_17, voice_18, voice_19,
				voice_20, voice_21, voice_22, voice_23, voice_24, voice_25, voice_26, voice_27, voice_28, voice_29,
				voice_30, voice_31, voice_32, voice_33, voice_34, voice_35, voice_36, voice_37, voice_38, voice_39,
				voice_40, voice_41, voice_42, voice_43, voice_44, voice_45, voice_46, voice_47 };
		for(int i=0 ; i < 48;i++){
			voice[i].setText(String.valueOf(i+1)+":");
		}
	}

}
