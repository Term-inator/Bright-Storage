package com.example.bright_storage.service.base;

import com.example.bright_storage.repository.BaseRepository;

public abstract class AbstractCrudService<ENTITY, ID> implements CrudService<ENTITY, ID>{

    protected BaseRepository<ENTITY, ID> repository;

    AbstractCrudService(){

    }
}
