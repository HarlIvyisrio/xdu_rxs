package com.xdurxs.rxs.kv.biz.domain.repository;

import com.xdurxs.rxs.kv.biz.domain.dataobject.NoteContentDO;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;

public interface NoteContentRepository extends CassandraRepository<NoteContentDO, UUID> {
}
