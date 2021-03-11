package me.ialext.abi.core.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.ialext.abi.api.data.ObjectRepository;
import me.ialext.abi.api.model.SavableModel;
import me.yushust.inject.assisted.Assist;
import me.yushust.inject.assisted.Assisted;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class JSONObjectRepository<O extends SavableModel> implements ObjectRepository<O> {

  private final ObjectMapper objectMapper;
  private final File folder;
  private final Class<O> modelClass;

  @Assisted public JSONObjectRepository(
      ObjectMapper objectMapper,
      File folder,
      Class<O> modelClass,
      @Assist String path,
      @Assist String folderName
  ) {
    this.objectMapper = objectMapper;
    this.folder = new File(path + File.pathSeparator + folderName);
    this.modelClass = modelClass;
  }

  @Override public Optional<O> findOneSync(String id) {
    File file = new File(folder, id + ".json");

    if (!file.exists()) {
      return Optional.empty();
    }

    O o = null;
    try {
      o = objectMapper.readValue(file, modelClass);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return Optional.ofNullable(o);
  }

  @Override public Set<O> findAllSync() {
    Set<O> objects = new HashSet<>();

    Arrays
        .stream(Objects.requireNonNull(folder.listFiles()))
        .filter(File::exists)
        .forEach(file -> {
          O o;

          try {
            o = objectMapper.readValue(file, modelClass);
            objects.add(o);
          } catch (IOException e) {
            e.printStackTrace();
          }
        });

    return objects;
  }

  @Override public void deleteSync(String id) {
    File file = new File(folder, id + ".json");

    if (!file.exists()) {
      return;
    }

    file.delete();
  }

  @Override public void replaceSync(String id, O o) {
    File file = new File(id + ".json");

    if (!file.exists()) {
      return;
    }

    try {
      objectMapper.writeValue(file, o);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override public void saveSync(O o) {
    File file = new File(o.getId() + ".json");

    try {
      objectMapper.writeValue(file, o);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
