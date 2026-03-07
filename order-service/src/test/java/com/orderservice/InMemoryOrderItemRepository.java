package com.orderservice;

import com.orderservice.domain.OrderItemEntity;
import com.orderservice.domain.OrderItemJpaRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

class InMemoryOrderItemRepository implements OrderItemJpaRepository {
    @Override
    public void flush() {

    }

    @Override
    public <S extends OrderItemEntity> S saveAndFlush(final S entity) {
        return null;
    }

    @Override
    public <S extends OrderItemEntity> List<S> saveAllAndFlush(final Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(final Iterable<OrderItemEntity> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(final Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public OrderItemEntity getOne(final Long aLong) {
        return null;
    }

    @Override
    public OrderItemEntity getById(final Long aLong) {
        return null;
    }

    @Override
    public OrderItemEntity getReferenceById(final Long aLong) {
        return null;
    }

    @Override
    public <S extends OrderItemEntity> List<S> findAll(final Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends OrderItemEntity> List<S> findAll(final Example<S> example, final Sort sort) {
        return List.of();
    }

    @Override
    public <S extends OrderItemEntity> List<S> saveAll(final Iterable<S> entities) {
        return List.of();
    }

    @Override
    public List<OrderItemEntity> findAll() {
        return List.of();
    }

    @Override
    public List<OrderItemEntity> findAllById(final Iterable<Long> longs) {
        return List.of();
    }

    @Override
    public <S extends OrderItemEntity> S save(final S entity) {
        return null;
    }

    @Override
    public Optional<OrderItemEntity> findById(final Long aLong) {
        return Optional.empty();
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
    public void delete(final OrderItemEntity entity) {

    }

    @Override
    public void deleteAllById(final Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(final Iterable<? extends OrderItemEntity> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<OrderItemEntity> findAll(final Sort sort) {
        return List.of();
    }

    @Override
    public Page<OrderItemEntity> findAll(final Pageable pageable) {
        return null;
    }

    @Override
    public <S extends OrderItemEntity> Optional<S> findOne(final Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends OrderItemEntity> Page<S> findAll(final Example<S> example, final Pageable pageable) {
        return null;
    }

    @Override
    public <S extends OrderItemEntity> long count(final Example<S> example) {
        return 0;
    }

    @Override
    public <S extends OrderItemEntity> boolean exists(final Example<S> example) {
        return false;
    }

    @Override
    public <S extends OrderItemEntity, R> R findBy(final Example<S> example, final Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
