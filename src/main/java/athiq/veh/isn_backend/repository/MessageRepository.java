package athiq.veh.isn_backend.repository;

import athiq.veh.isn_backend.model.Message;
import athiq.veh.isn_backend.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {


    @Query("SELECT m FROM Message m WHERE (m.sender = :loggedInUser AND m.receiver = :otherUser) " +
            "OR (m.sender = :otherUser AND m.receiver = :loggedInUser) " +
            "ORDER BY m.timestamp ASC")
    List<Message> findMessagesBetweenUsers(@Param("loggedInUser") User loggedInUser,
                                           @Param("otherUser") User otherUser);


//	   @Query("SELECT DISTINCT m.sender FROM Message m WHERE m.receiver = :loggedInUser")
//	   List<User> findUsersWhoMessaged(@Param("loggedInUser") User loggedInUser);

//
//
//    List<Message> findByReceiverId(Long receiverId);
//
//    List<Message> findBySenderId(Long senderId);

    @Query("SELECT DISTINCT m.sender FROM Message m WHERE m.receiver = :receiver")
    List<User> findUsersWhoMessaged(@Param("receiver") User receiver);


//    @Modifying
//    @Transactional
//    @Query("UPDATE Message m SET m.isRead = true WHERE m.receiver = :receiver AND m.isRead = false")
//    void markMessagesAsRead(@Param("receiver") User receiver);

    @Modifying
    @Transactional
    @Query("UPDATE Message m SET m.isRead = true WHERE m.receiver = :receiver AND m.sender = :sender AND m.isRead = false")
    void markMessagesAsRead(@Param("receiver") User receiver, @Param("sender") User sender);



    @Query("SELECT m FROM Message m WHERE m.receiver = :user AND m.isRead = :isRead")
    List<Message> findMessagesByReadStatus(@Param("user") User user, @Param("isRead") boolean isRead);

}

