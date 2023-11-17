public enum Type {
  SQUARE,
  NOISE;
  public String toString() {
    switch(this) {
      case SQUARE: return "Square";
      case NOISE: return "Noise";
      default: throw new IllegalArgumentException();
    }
  }
}