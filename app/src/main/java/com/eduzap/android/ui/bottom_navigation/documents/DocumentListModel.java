package com.eduzap.android.ui.bottom_navigation.documents;

public class DocumentListModel {
    private String documentName, documentDescription, documentUrl;

    public DocumentListModel(String documentName, String documentDescription, String documentUrl) {
        this.documentName = documentName;
        this.documentDescription = documentDescription;
        this.documentUrl = documentUrl;
    }

    public DocumentListModel() {
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentDescription() {
        return documentDescription;
    }

    public void setDocumentDescription(String documentDescription) {
        this.documentDescription = documentDescription;
    }
}
