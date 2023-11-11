public enum Type {
  CARRE,
  BLANC;
  public String toString() {
    switch(this) {
      case CARRE: return "Carre";
      case BLANC: return "Blanc";
      default: throw new IllegalArgumentException();
    }
  }
}