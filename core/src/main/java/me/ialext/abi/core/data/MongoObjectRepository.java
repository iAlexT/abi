package me.ialext.abi.core.data;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.ialext.abi.api.data.ObjectRepository;
import me.ialext.abi.api.model.SavableModel;
import me.yushust.inject.assisted.Assist;
import me.yushust.inject.assisted.Assisted;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.mongodb.client.model.Filters.eq;

public class MongoObjectRepository<O extends SavableModel> implements ObjectRepository<O> {

  private final MongoCollection<O> mongoCollection;

  @Assisted public MongoObjectRepository(
      MongoDatabase mongoDatabase,
      @Assist String collectionKey,
      Class<O> modelClass
  ) {
    this.mongoCollection = mongoDatabase.getCollection(collectionKey, modelClass);
  }

  @Override public Optional<O> findOneSync(String id) {
    return Optional.ofNullable(mongoCollection.find(eq("_id", id)).first());
  }

  @Override public Set<O> findAllSync() {
    return mongoCollection.find().into(new HashSet<>());
  }

  @Override public void deleteSync(String id) {
    mongoCollection.findOneAndDelete(eq("_id", id));
  }

  @Override public void replaceSync(String id, O o) {
    mongoCollection.findOneAndReplace(eq("_id, id"), o);
  }

  @Override public void saveSync(O o) {
    mongoCollection.insertOne(o);
  }
}
