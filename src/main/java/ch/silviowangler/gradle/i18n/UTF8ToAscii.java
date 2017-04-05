package ch.silviowangler.gradle.i18n;

/**
 * @author Silvio Wangler
 */
public interface UTF8ToAscii {


  static final char[] hexChar = {
      '0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'
  };

  static String unicodeEscape(String s) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if ((c >> 7) > 0) {
        sb.append("\\u");
        sb.append(hexChar[(c >> 12) & 0xF]); // append the hex character for the left-most 4-bits
        sb.append(hexChar[(c >> 8) & 0xF]);  // hex for the second group of 4-bits from the left
        sb.append(hexChar[(c >> 4) & 0xF]);  // hex for the third group
        sb.append(hexChar[c & 0xF]);         // hex for the last group, e.g., the right most 4-bits
      }
      else {
        sb.append(c);
      }
    }
    return sb.toString();
  }
}
