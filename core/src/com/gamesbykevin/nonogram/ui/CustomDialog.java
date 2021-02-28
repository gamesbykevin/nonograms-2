package com.gamesbykevin.nonogram.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import static com.gamesbykevin.nonogram.util.ButtonHelper.STYLE_BUTTON_SMALL_BLUE;
import static com.gamesbykevin.nonogram.util.LabelHelper.getStyleLabelNormal;

public class CustomDialog extends Dialog {

    public static final String STYLE_DIALOG_DEFAULT = "default";

    //size of the dialog
    private float width = 100, height = 100;

    private String labelStyle;

    public CustomDialog(Skin skin) {
        this("", skin, STYLE_DIALOG_DEFAULT);
    }

    public CustomDialog(String title, Skin skin) {
        this(title, skin, STYLE_DIALOG_DEFAULT);
    }

    public CustomDialog(Skin skin, String dialogStyle) {
        this("", skin, dialogStyle);
    }

    public CustomDialog(String title, Skin skin, String dialogStyle) {
        super(title, skin, dialogStyle);
        initialize();
        getStyle().stageBackground = null;
    }

    public void setLabelStyle(String labelStyle) {
        this.labelStyle = labelStyle;
    }

    public String getLabelStyle() {
        return labelStyle;
    }

    private void initialize() {
        setModal(true);
        setMovable(false);
        setResizable(false);
        setLabelStyle(getStyleLabelNormal());
    }

    @Override
    public CustomDialog text(String text) {
        super.text(new Label(text, getSkin(), getLabelStyle()));
        return this;
    }

    public CustomDialog button(String buttonText, Object object) {
        return button(buttonText, object, STYLE_BUTTON_SMALL_BLUE);
    }

    public CustomDialog button(String buttonText, Object object, String style) {
        return button(buttonText, object, style, null);
    }
    /**
     * Adds a text button to the button table.
     * @param object the result when the button is actioned.
     */
    public CustomDialog button(String buttonText, Object object, String style, String name) {
        TextButton button = new TextButton(buttonText, getSkin(), style);
        button.setName(name);
        super.button(button, object);
        return this;
    }

    @Override
    public float getPrefWidth() {
        // force dialog width
        return this.getWidth();
    }

    @Override
    public float getPrefHeight() {
        // force dialog height
        return this.getHeight();
    }

    @Override
    public float getHeight () {
        return this.height;
    }

    @Override
    public float getWidth() {
        return this.width;
    }

    @Override
    public void setWidth(float width) {
        setSize(width, getHeight());
    }

    @Override
    public void setHeight(float height) {
        setSize(getWidth(), height);
    }

    @Override
    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }
}