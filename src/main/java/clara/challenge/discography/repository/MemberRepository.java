package clara.challenge.discography.repository;

import clara.challenge.discography.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
