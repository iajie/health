package com.ajie.dao;

import com.ajie.entity.Member;
import com.github.pagehelper.Page;

import java.util.List;

/*
 *  @Author 阿杰
 *  @create 2021年01月08日 15:21
 */
public interface MemberDao {
    List<Member> findAll();

    Page<Member> findByCondition(String queryString);

    void add(Member member);

    void deleteById(Integer id);

    Member findById(Integer id);

    Member findByTelephone(String telephone);

    void edit(Member member);

    Integer findMemberCountBeforeDate(String date);

    Integer findMemberCountDate(String date);

    Integer findMemberCountAfterDate(String date);

    Integer findMemberTotalCount();
}
