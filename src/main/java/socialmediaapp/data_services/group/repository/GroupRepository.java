package socialmediaapp.data_services.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import socialmediaapp.data_services.group.model.Group;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, String> {

    @Query("""
        select g.id, g.groupName, g.groupImage
        from Group g
    """)
    List<Object[]> findAllGroups();

    @Query("""
        select
        g.id,
        g.groupName,
        g.groupDescription,
        g.groupImage,
        g.whoCanCreatePosts,
        g.whoCanSeePosts,
        g.howToJoin
        from Group g
        where g.id = ?1
    """)
    Optional<Object[]> findGroupById(String id);

    @Query("""
       select
        case when COUNT(gu) > 0
            then true
            else false
        end
       from Group g join g.groupUsers gu
       where g.id = ?1 and gu.user.uuid = ?2
    """)
    boolean isUserAGroupMember(String groupId, String userId);

    @Query("""
       select
        case when COUNT(gu) > 0
            then true
            else false
        end
       from Group g join g.bannedUsers gu
       where g.id = ?1 and gu.user.uuid = ?2
    """)
    boolean isUserBannedFromGroup(String groupId, String userId);

    /*
    @Query("""
                select count (gbu) > 0
                from Group.bannedUsers gbu
                where gbu.user.uuid = ?1 and gbu.group.id = ?2
            """)
    boolean isUserABannedMemberOfGroup(String userId, String groupId);


    @Query("""
                    select gu
                    from GroupUser gu
                    where gu.group.id = ?1
            """)
    List<GroupUser> findUsersByGroup(String groupId);



    @Query("""
                select pg
                from GroupPost pg
                where pg.group.id = ?1
                order by pg.post.createdAt desc
            """)
    List<Post> findAllPostsByGroup(String groupId, Pageable pageable);
    */

}
