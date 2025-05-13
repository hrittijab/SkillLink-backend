package com.skilllink.repository;

import com.skilllink.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    // ✅ 1. All messages between two users (ordered)
    @Query("SELECT m FROM Message m WHERE " +
           "(LOWER(m.senderEmail) = LOWER(:user1) AND LOWER(m.receiverEmail) = LOWER(:user2)) OR " +
           "(LOWER(m.senderEmail) = LOWER(:user2) AND LOWER(m.receiverEmail) = LOWER(:user1)) " +
           "ORDER BY m.timestamp ASC")
    List<Message> findConversationBetween(@Param("user1") String user1, @Param("user2") String user2);

    // ✅ 2. All messages involving a user
    List<Message> findBySenderEmailOrReceiverEmailOrderByTimestampAsc(String senderEmail, String receiverEmail);

    // ✅ 3. All unique chat partners of a user
    @Query("SELECT DISTINCT CASE " +
           "WHEN LOWER(m.senderEmail) = LOWER(:email) THEN LOWER(m.receiverEmail) " +
           "ELSE LOWER(m.senderEmail) END " +
           "FROM Message m WHERE LOWER(m.senderEmail) = LOWER(:email) OR LOWER(m.receiverEmail) = LOWER(:email)")
    List<String> findDistinctChatPartners(@Param("email") String email);

    // ✅ 4. FIXED: One latest message per contact for previews (PostgreSQL-friendly)
    @Query(value = """
        SELECT DISTINCT ON (chat_partner) *
        FROM (
            SELECT *,
                CASE
                    WHEN LOWER(sender_email) = LOWER(:email) THEN LOWER(receiver_email)
                    ELSE LOWER(sender_email)
                END AS chat_partner
            FROM messages
            WHERE LOWER(sender_email) = LOWER(:email) OR LOWER(receiver_email) = LOWER(:email)
        ) AS sub
        ORDER BY chat_partner, timestamp DESC
        """, nativeQuery = true)
    List<Message> findLatestMessagesPerContact(@Param("email") String email);
}
