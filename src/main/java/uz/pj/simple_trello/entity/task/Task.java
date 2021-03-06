package uz.pj.simple_trello.entity.task;

import lombok.Getter;
import lombok.Setter;
import uz.pj.simple_trello.entity.base.Auditable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class Task extends Auditable {

    @Column(nullable = false)
    private String name;

    private String description;


    private String level;

    private Long priority;

    private Long projectId;
    private Long columnId;

    @Column(columnDefinition = " timestamp ")
    private LocalDateTime deadline;

    private boolean completed;


}
