package ru.yandex.practicum.mymarket.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.dto.PagingDto;
import ru.yandex.practicum.mymarket.dto.cart.CartItemAction;
import ru.yandex.practicum.mymarket.dto.item.ItemDto;
import ru.yandex.practicum.mymarket.dto.item.ItemSearchRequestDto;
import ru.yandex.practicum.mymarket.service.item.ItemService;
import ru.yandex.practicum.mymarket.utils.PartitionUtils;

@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private static final ItemDto emptyItemDto = ItemDto.builder().id(-1L).build();

    private final ItemService itemService;

    @GetMapping("/{id}")
    public Mono<String> getItemById(@PathVariable("id") @NotNull Long itemId, Model model) {
        log.info("GET /item with params(id={}))", itemId);

        return itemService.getItemById(itemId)
                .doOnNext(item -> model.addAttribute("item", item))
                .thenReturn("item");
    }

    @GetMapping
    public Mono<String> getItems(@ModelAttribute ItemSearchRequestDto itemSearchRequestDto, Model model) {
        log.info("GET /items with params: {}", itemSearchRequestDto);

        return itemService.findItems(itemSearchRequestDto)
                .doOnNext(page -> {
                    model.addAttribute("items", PartitionUtils.chunkAndPad(page.getContent(), 3, emptyItemDto));
                    model.addAttribute("search", itemSearchRequestDto.search());
                    model.addAttribute("sort", itemSearchRequestDto.sort().toString());
                    model.addAttribute("paging", new PagingDto(page));
                })
                .thenReturn("items");
    }

    @PostMapping
    public Mono<String> changeItems(@RequestParam("id") Long itemId, @RequestParam("action") CartItemAction cartItemAction,
                                    @ModelAttribute ItemSearchRequestDto itemSearchRequestDto) {
        log.info("POST /items with params(id={}, action={}): {}", itemId, cartItemAction, itemSearchRequestDto);

        return itemService.changeItemCount(itemId, cartItemAction)
                .thenReturn("redirect:" + UriComponentsBuilder
                        .fromPath("/items")
                        .queryParam("search", itemSearchRequestDto.search())
                        .queryParam("sort", itemSearchRequestDto.sort())
                        .queryParam("pageNumber", itemSearchRequestDto.pageNumber())
                        .queryParam("pageSize", itemSearchRequestDto.pageSize())
                        .build()
                        .toUriString()
                );

    }

    @PostMapping("/{id}")
    public Mono<String> changeItems(@PathVariable("id") Long itemId, @RequestParam("action") CartItemAction cartItemAction) {
        log.info("POST /items/{} with params(action={})", itemId, cartItemAction);

        return itemService.changeItemCount(itemId, cartItemAction)
                .thenReturn("redirect:" + UriComponentsBuilder
                        .fromPath("/items")
                        .pathSegment(itemId.toString())
                        .build()
                        .toUriString()
                );
    }
}