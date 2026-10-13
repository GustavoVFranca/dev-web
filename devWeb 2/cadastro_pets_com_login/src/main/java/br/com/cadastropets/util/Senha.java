package br.com.cadastropets.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Senha {

    // Transforma a senha em hash SHA-256 (nao guardamos a senha pura)
    public static String hash(String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(senha.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
