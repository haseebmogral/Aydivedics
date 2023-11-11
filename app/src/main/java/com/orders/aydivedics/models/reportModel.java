package com.orders.aydivedics.models;

public class reportModel {
    public String productId;

    public reportModel(String productId, String productName, int prepaidOrders, int prepaidOrderPieces, int codOrders, int codOrderPieces) {
        this.productId = productId;
        this.productName = productName;
        this.prepaidOrders = prepaidOrders;
        this.prepaidOrderPieces = prepaidOrderPieces;
        this.codOrders = codOrders;
        this.codOrderPieces = codOrderPieces;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getPrepaidOrders() {
        return prepaidOrders;
    }

    public void setPrepaidOrders(int prepaidOrders) {
        this.prepaidOrders = prepaidOrders;
    }

    public int getPrepaidOrderPieces() {
        return prepaidOrderPieces;
    }

    public void setPrepaidOrderPieces(int prepaidOrderPieces) {
        this.prepaidOrderPieces = prepaidOrderPieces;
    }

    public int getCodOrders() {
        return codOrders;
    }

    public void setCodOrders(int codOrders) {
        this.codOrders = codOrders;
    }

    public int getCodOrderPieces() {
        return codOrderPieces;
    }

    public void setCodOrderPieces(int codOrderPieces) {
        this.codOrderPieces = codOrderPieces;
    }

    public String productName;
    public int prepaidOrders;
    public int prepaidOrderPieces;
    public int codOrders;
    public int codOrderPieces;
}
