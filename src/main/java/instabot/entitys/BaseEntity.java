package instabot.entitys;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
public abstract class BaseEntity implements Serializable{


    @Id @GeneratedValue
    private Long id;

    private String uuid = UUID.randomUUID().toString();

    public String getUuid() {
        return uuid;
    }

    public int hashCode(){
        return Objects.hash(uuid);
    }
    public boolean equals(Object that){
        return this == that || that instanceof BaseEntity
                && Objects.equals(uuid, ((BaseEntity)that).uuid);

    }
}
