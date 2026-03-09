package com.orderservice;

import com.orderservice.domain.db.OrderEntity;
import com.orderservice.domain.db.OrderJpaRepository;
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

class InMemoryOrderRepository implements OrderJpaRepository {

    private final List<OrderEntity> database = new ArrayList<>();
    private final AtomicLong index = new AtomicLong(1);

    @Override
    public void flush() {

    }

    @Override
    public <S extends OrderEntity> S saveAndFlush(final S entity) {
        return null;
    }

    @Override
    public <S extends OrderEntity> List<S> saveAllAndFlush(final Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(final Iterable<OrderEntity> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(final Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public OrderEntity getOne(final Long aLong) {
        return null;
    }

    @Override
    public OrderEntity getById(final Long aLong) {
        return null;
    }

    @Override
    public OrderEntity getReferenceById(final Long aLong) {
        return null;
    }

    @Override
    public <S extends OrderEntity> Optional<S> findOne(final Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends OrderEntity> List<S> findAll(final Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends OrderEntity> List<S> findAll(final Example<S> example, final Sort sort) {
        return List.of();
    }

    @Override
    public <S extends OrderEntity> Page<S> findAll(final Example<S> example, final Pageable pageable) {
        return null;
    }

    @Override
    public <S extends OrderEntity> long count(final Example<S> example) {
        return 0;
    }

    @Override
    public <S extends OrderEntity> boolean exists(final Example<S> example) {
        return false;
    }

    @Override
    public <S extends OrderEntity, R> R findBy(final Example<S> example, final Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends OrderEntity> S save(final S entity) {

        if (entity.getId() == null) {
            entity.setId(index.getAndIncrement());
        }

        database.add(entity);

        return entity;
    }


    @Override
    public <S extends OrderEntity> List<S> saveAll(final Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<OrderEntity> findById(final Long aLong) {
        return database.stream().filter(order -> order.getId().equals(aLong)).findFirst();
    }

    @Override
    public boolean existsById(final Long aLong) {
        return false;
    }

    @Override
    public List<OrderEntity> findAll() {
        return List.of();
    }

    @Override
    public List<OrderEntity> findAllById(final Iterable<Long> longs) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(final Long aLong) {

    }

    @Override
    public void delete(final OrderEntity entity) {

    }

    @Override
    public void deleteAllById(final Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(final Iterable<? extends OrderEntity> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<OrderEntity> findAll(final Sort sort) {
        return List.of();
    }

    @Override
    public Page<OrderEntity> findAll(final Pageable pageable) {
        return null;
    }
}
