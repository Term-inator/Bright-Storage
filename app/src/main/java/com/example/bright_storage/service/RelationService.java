package com.example.bright_storage.service;

import com.example.bright_storage.model.entity.Relation;

import java.util.List;

public interface RelationService {

    void joinRelation();

    Relation getRelationById(Long id);

    List<Relation> listCurrentUserRelation();
}
