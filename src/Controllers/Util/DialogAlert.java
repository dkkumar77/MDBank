package Controllers.Util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public final class DialogAlert
{

	public static JFXDialog showOKDialog(StackPane stackPane, String message)
	{
		JFXDialogLayout content = new JFXDialogLayout();
		content.setBody(new Text(message));
		JFXDialog dialog = new JFXDialog(stackPane,content,JFXDialog.DialogTransition.CENTER);
		JFXButton okButton = new JFXButton("Ok");
		okButton.setButtonType(JFXButton.ButtonType.RAISED);
		okButton.setOnAction(event -> dialog.close());
		content.setActions(okButton);
		dialog.show();
		return dialog;
	}

}
