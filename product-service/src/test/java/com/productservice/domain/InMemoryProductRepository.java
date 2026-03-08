package com.productservice.domain;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

class InMemoryProductRepository implements ProductJpaRepository {

    private final List<ProductEntity> database = new ArrayList<>();
    private final AtomicLong index = new AtomicLong(1);

    @Override
    public void flush() {

    }

    @Override
    public <S extends ProductEntity> S saveAndFlush(final S entity) {
        return null;
    }

    @Override
    public <S extends ProductEntity> List<S> saveAllAndFlush(final Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(final Iterable<ProductEntity> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(final Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public ProductEntity getOne(final Long aLong) {
        return null;
    }

    @Override
    public ProductEntity getById(final Long aLong) {
        return null;
    }

    @Override
    public ProductEntity getReferenceById(final Long aLong) {
        return null;
    }

    @Override
    public <S extends ProductEntity> List<S> findAll(final Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends ProductEntity> List<S> findAll(final Example<S> example, final Sort sort) {
        return List.of();
    }

    @Override
    public <S extends ProductEntity> List<S> saveAll(final Iterable<S> entities) {
        return List.of();
    }

    @Override
    public List<ProductEntity> findAll() {
        return database.stream().toList();
    }

    @Override
    public List<ProductEntity> findAllById(final Iterable<Long> longs) {
        return List.of();
    }

    @Override
    public <S extends ProductEntity> S save(final S entity) {
        if (entity.getId() == null) {
            entity.setId(index.getAndIncrement());
        }
        database.add(entity);
        return entity;
    }

    @Override
    public Optional<ProductEntity> findById(final Long aLong) {
        return database.stream().filter(product -> product.getId().equals(aLong)).findFirst();
    }

    @Override
    public boolean existsById(final Long aLong) {
        return false;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(final Long aLong) {
    }

    @Override
    public void delete(final ProductEntity entity) {
        database.remove(entity);
    }

    @Override
    public void deleteAllById(final Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(final Iterable<? extends ProductEntity> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<ProductEntity> findAll(final Sort sort) {
        return List.of();
    }

    @Override
    public Page<ProductEntity> findAll(final Pageable pageable) {
        return null;
    }

    @Override
    public <S extends ProductEntity> Optional<S> findOne(final Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends ProductEntity> Page<S> findAll(final Example<S> example, final Pageable pageable) {
        return null;
    }

    @Override
    public <S extends ProductEntity> long count(final Example<S> example) {
        return 0;
    }

    @Override
    public <S extends ProductEntity> boolean exists(final Example<S> example) {
        return false;
    }

    @Override
    public <S extends ProductEntity, R> R findBy(final Example<S> example, final Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
