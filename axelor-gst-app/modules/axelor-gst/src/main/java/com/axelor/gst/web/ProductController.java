package com.axelor.gst.web;

import java.io.File;
import com.axelor.app.AppSettings;
import com.axelor.db.JpaSupport;
import com.axelor.gst.db.Product;
import com.axelor.gst.db.ProductCategory;
import com.axelor.gst.service.InvoiceLineService;
import com.axelor.gst.service.InvoiceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class ProductController extends JpaSupport {
  @Inject InvoiceService invoiceservice;
  @Inject InvoiceLineService invoicelineservice;

  public void setGstRate(ActionRequest request, ActionResponse response) {
    Product product = request.getContext().asType(Product.class);
    ProductCategory productCategory = product.getCategory();
    response.setValue("gstRate", productCategory != null ? productCategory.getGstRate() : null);
  }

  public void setReportParam(ActionRequest request, ActionResponse response) {
    if (request.getContext().get("_ids") != null) {
      String ids = request.getContext().get("_ids").toString();
      String newids = ids.substring(1, ids.length() - 1);
      String attachmentPath = AppSettings.get().getPath("file.upload.dir", "");
      attachmentPath =
          attachmentPath.endsWith(File.separator)
              ? attachmentPath
              : attachmentPath + File.separator;
      request.getContext().put("attachments", attachmentPath);
      request.getContext().put("productIds", newids);
    } else {
      response.setFlash("No Product Selected");
    }
  }
}
