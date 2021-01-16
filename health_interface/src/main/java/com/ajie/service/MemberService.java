package com.ajie.service;

import com.ajie.entity.Member;

import java.util.List;
import java.util.Map;

/*
 *  @Author 阿杰
 *  @create 2021年01月08日 21:01
 */
public interface MemberService {
    Member findByTelephone(String telephone);

    void add(Member member);

    List<Integer> findMemberCountByMonth(List<String> months);
}
