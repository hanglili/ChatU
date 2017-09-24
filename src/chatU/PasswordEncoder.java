package chatU;

public class PasswordEncoder {

  public int encode(String code) {
    return code.hashCode();
  }

}
