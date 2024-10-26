package org.rmerezha.service;

import java.io.InputStream;

public interface Service {

    String get(InputStream jsonStream);

    String add(InputStream jsonStream);

    String update(InputStream jsonStream);

    String remove(InputStream jsonStream);

}
