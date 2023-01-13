package ru.trick.springMangaBot.DAO;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.trick.springMangaBot.config.BotConfig;
import ru.trick.springMangaBot.model.ChapterManga;

@Component
public class MangaDAO {

    private final JdbcTemplate jdbcTemplate;
    private final BotConfig botConfig;

    @Autowired
    public MangaDAO(JdbcTemplate jdbcTemplate, BotConfig botConfig) {
        this.jdbcTemplate = jdbcTemplate;
        this.botConfig = botConfig;
    }



    public ChapterManga takeChapter (int chapterManga) {
        return jdbcTemplate.query("SELECT * FROM chapter_empror WHERE chapter_manga=?", new Object[]{chapterManga}, new BeanPropertyRowMapper<>(ChapterManga.class))
                .stream().findAny().orElse(null);
    }

}
