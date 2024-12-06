package com.example.jwt_refresh_practice.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import com.example.jwt_refresh_practice.entity.Member;

@DataJpaTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MemberRepositoryTest {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberRepositoryTest(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @BeforeAll
    public void createMember() {
        Member member = Member.builder()
                .username("username1")
                .password("password")
                .role("ROLE_USER")
                .build();

        memberRepository.save(member);
    }

    @Test
    @Rollback(true)
    @DisplayName("저장한 username이 존재하는지 확인한다.")
    public void testExistsByUsername() {
        
        assertTrue(memberRepository.existsByUsername("username1"));
        assertFalse(memberRepository.existsByUsername("username"));
        assertFalse(memberRepository.existsByUsername(""));
        assertFalse(memberRepository.existsByUsername("username2"));
    }

    @Test
    @Rollback(true)
    @DisplayName("username Member를 검색할 수 있는지 확인한다.")
    public void testFindByUsername() {

        Optional<Member> existMember = memberRepository.findByUsername("username1");
        Optional<Member> notExistMember = memberRepository.findByUsername("username");

        assertThat(existMember).isPresent();
        assertThat(existMember.get().getUsername()).isEqualTo("username1");
        
        assertThat(notExistMember).isNotPresent();
    }
}
