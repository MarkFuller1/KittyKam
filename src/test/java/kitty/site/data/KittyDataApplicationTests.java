package kitty.site.data;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitty.site.data.service.KittyProcessingService.collate;

@SpringBootTest
class KittyDataApplicationTests {




    @Test
    void CollateTest1() {
        List<Integer> a = new ArrayList<>();
        listAdd(a, 1, 2, 3, 3, 1, 2, 15, 16, 14, 15, 13, 6, 7, 8, 9);
    }

    private void listAdd(List<Integer> list, Integer... additives) {
        list.addAll(Arrays.asList(additives));
    }
}
