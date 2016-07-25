package com.eyelinecom.whoisd.sads2.msbotframework.api.model.attachments;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class ReceiptCard extends Card<ReceiptCard> {

  /**
   * Title of the card
   */
  private String title;

  private ReceiptItem[] items;

  private Fact[] facts;

  /**
   * This action will be activated when user taps on the card
   */
  private CardAction tap;

  /**
   * Total amount of money paid (or should be paid)
   */
  private String total;

  /**]
   * Total amount of TAX paid (or should be paid)
   */
  private String tax;

  /**
   * Total amount of VAT paid (or should be paid)
   */
  private String vat;

  /**
   * Set of actions applicable to the current card.
   */
  private CardAction[] buttons;

  public ReceiptCard() {}

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public ReceiptItem[] getItems() {
    return items;
  }

  public void setItems(ReceiptItem[] items) {
    this.items = items;
  }

  @JsonIgnore
  public void setItems(List<ReceiptItem> items) {
    this.setItems(items.toArray(new ReceiptItem[0]));
  }

  public Fact[] getFacts() {
    return facts;
  }

  public void setFacts(Fact[] facts) {
    this.facts = facts;
  }

  public CardAction getTap() {
    return tap;
  }

  public void setTap(CardAction tap) {
    this.tap = tap;
  }

  public String getTotal() {
    return total;
  }

  public void setTotal(String total) {
    this.total = total;
  }

  public String getTax() {
    return tax;
  }

  public void setTax(String tax) {
    this.tax = tax;
  }

  public String getVat() {
    return vat;
  }

  public void setVat(String vat) {
    this.vat = vat;
  }

  public CardAction[] getButtons() {
    return buttons;
  }

  public void setButtons(CardAction[] buttons) {
    this.buttons = buttons;
  }

  @Override
  @JsonIgnore
  protected final String getContentType() {
    return "application/vnd.microsoft.card.receipt";
  }
}
