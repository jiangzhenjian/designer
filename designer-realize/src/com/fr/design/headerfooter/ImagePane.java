/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.headerfooter;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.ImgChooseWrapper;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.style.background.image.ImageFileChooser;
import com.fr.design.style.background.image.ImagePreviewPane;
import com.fr.general.Inter;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Image Pane.
 */
public class ImagePane extends BasicPane {
    private ImagePreviewPane imagePreviewPane;
    private ImageFileChooser imageFileChooser = null;

    public ImagePane() {
        this(true);
    }

    public ImagePane(boolean hasPreviewBorder) {
        this.setLayout(FRGUIPaneFactory.createM_BorderLayout());

        //preview pane.
        JPanel previewPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        this.add(previewPane);

//        previewPane.setLayout(FRGUIPaneFactory.createBorderLayout());
        if (hasPreviewBorder) {
            previewPane.setBorder(BorderFactory.createTitledBorder(Inter.getLocText("Preview")));
        }

        imagePreviewPane = new ImagePreviewPane();
        previewPane.add(new JScrollPane(imagePreviewPane));

        //select image
        JPanel rightPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        this.add(rightPane, BorderLayout.EAST);

//        rightPane.setLayout(FRGUIPaneFactory.createBorderLayout());
        if (hasPreviewBorder) {
            rightPane.setBorder(BorderFactory.createEmptyBorder(8, 2, 0, 2));
        } else {
            rightPane.setBorder(BorderFactory.createEmptyBorder(4, 2, 0, 2));
        }
        UIButton selectImageButton = new UIButton(Inter.getLocText("Image-Select_Picture"));
        rightPane.add(selectImageButton, BorderLayout.NORTH);
        selectImageButton.addActionListener(selectPictureActionListener);

        //init image file chooser.
        imageFileChooser = new ImageFileChooser();
        imageFileChooser.setMultiSelectionEnabled(false);
    }

    @Override
    protected String title4PopupWindow() {
        return "image";
    }

    public void populate(Image image) {
        if(image == null) {
            return;
        }

        this.imagePreviewPane.setImage(image);
    }

    public Image update() {
        return this.imagePreviewPane.getImage();
    }

    /**
     * Select picture.
     */
    ActionListener selectPictureActionListener = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            int returnVal = imageFileChooser.showOpenDialog(ImagePane.this);
            ImgChooseWrapper.getInstance(imagePreviewPane, imageFileChooser, null).dealWithImageFile(returnVal);
        }
    };
}