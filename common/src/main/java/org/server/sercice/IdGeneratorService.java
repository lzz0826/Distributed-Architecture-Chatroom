package org.server.sercice;

import org.server.util.IdGenerator;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class IdGeneratorService {

    private IdGenerator idGenerator;

    private Long workerId = 5L;
    private Long datacenterId = 5L;

    @PostConstruct
    private void init() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date epochDate = sdf.parse("2022-01-01");

        idGenerator = new IdGenerator(workerId, datacenterId, true, 100, epochDate, 3L, 3L, 20L);
    }

    public String getNextId() {
        return String.valueOf(idGenerator.nextId());
    }
}
