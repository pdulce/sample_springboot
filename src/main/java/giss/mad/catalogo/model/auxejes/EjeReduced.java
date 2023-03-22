package giss.mad.catalogo.model.auxejes;

public final class EjeReduced {

  private Integer num;

  private String eje;

  public EjeReduced(final Integer id, final String name) {
    this.num = id;
    this.eje = name;
  }

  public EjeReduced() {

  }

  public Integer getNum() {
    return num;
  }

  public void setNum(final Integer id) {
    this.num = id;
  }

  public String getEje() {
    return eje;
  }

  public void setEje(final String name) {
    this.eje = name;
  }
}
