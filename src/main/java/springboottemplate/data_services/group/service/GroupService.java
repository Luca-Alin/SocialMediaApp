package springboottemplate.data_services.group.service;

import lombok.RequiredArgsConstructor;
import springboottemplate.data_services.group.repository.GroupRepository;

@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;


}
