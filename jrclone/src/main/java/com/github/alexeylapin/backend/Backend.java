package com.github.alexeylapin.backend;


import com.github.alexeylapin.Item;

import java.util.List;

public interface Backend {

    List<Item> list(String path);

    List<Item> listDirs(String path);

}
