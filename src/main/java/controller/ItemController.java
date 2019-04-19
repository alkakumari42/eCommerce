package controller;

import model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import repository.ItemRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path ="/item")
public class ItemController {

    @Autowired
    private ItemRepository repository;

    @PostMapping(path = "/add")
    private @ResponseBody boolean addItem(@RequestBody final Item item){
         return repository.save(item) != null;

    }

    @GetMapping(path = "/get")
    private @ResponseBody Item getItem(final Integer itemId){
        final Optional<Item> item = repository.findById(itemId);
        return item.orElse(null);
    }

    @GetMapping(path = "/getAll")
    private @ResponseBody
    List<Item> getAllItems(){
        final List<Item> items = new ArrayList<>();
        repository.findAll().forEach(items::add);
        return items;
    }
}
