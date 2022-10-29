package com.ebook.multbooks.app.mybook.repository;

import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.mybook.entity.MyBook;
import com.ebook.multbooks.app.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface MyBookRepository extends JpaRepository<MyBook,Long> {

    Optional<MyBook> findByMemberAndProduct(Member member, Product product);

   List<MyBook> findByMember(Member member);
}
