package shame.astra.hud;

import dev.redstones.mediaplayerinfo.IMediaSession;
import dev.redstones.mediaplayerinfo.MediaInfo;
import dev.redstones.mediaplayerinfo.MediaPlayerInfo;

import shame.astra.Javelin;
import shame.astra.base.animations.base.Animation;
import shame.astra.base.animations.base.Easing;
import shame.astra.base.font.Fonts;
import shame.astra.base.theme.Theme;
import shame.astra.client.hud.elements.draggable.DraggableHudElement;
import shame.astra.utility.render.display.base.BorderRadius;
import shame.astra.utility.render.display.base.CustomDrawContext;
import shame.astra.utility.render.display.base.color.ColorRGBA;
import shame.astra.utility.render.display.shader.DrawUtil;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MediaPlayerComponent extends DraggableHudElement {
    private final Animation alpha;
    private MediaInfo currentMedia;
    private String currentOwner;
    private final ExecutorService executor;
    private volatile boolean isUpdating = false;
    private long lastUpdateTime = 0;
    private static final long UPDATE_INTERVAL = 100;
    private long lastSuccessfulUpdate = 0;
    private static final long HIDE_DELAY = 5000; // Скрывать только через 5 секунд без данных

    public MediaPlayerComponent(String name, float initialX, float initialY, float windowWidth, float windowHeight, float offsetX, float offsetY, Align align) {
        super(name, initialX, initialY, windowWidth, windowHeight, offsetX, offsetY, align);
        this.alpha = new Animation(200L, Easing.CUBIC_OUT);
        this.currentMedia = null;
        this.currentOwner = null;
        this.executor = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r, "MediaPlayer-Updater");
            thread.setDaemon(true);
            return thread;
        });
    }

    
    public void render(CustomDrawContext ctx) {
        float posX = this.getX();
        float posY = this.getY();
        
        // Асинхронное обновление медиа-информации
        long currentTime = System.currentTimeMillis();
        if (!isUpdating && (currentTime - lastUpdateTime) > UPDATE_INTERVAL) {
            isUpdating = true;
            lastUpdateTime = currentTime;
            
            CompletableFuture.runAsync(() -> {
                try {
                    List<IMediaSession> sessions = MediaPlayerInfo.INSTANCE.getMediaSessions();
                    if (!sessions.isEmpty()) {
                        // Обновляем время при любой успешной сессии
                        lastSuccessfulUpdate = System.currentTimeMillis();
                        
                        IMediaSession session = sessions.get(0);
                        MediaInfo newMedia = session.getMedia();
                        if (newMedia != null && newMedia.getTitle() != null) {
                            currentMedia = newMedia;
                            currentOwner = session.getOwner();
                        }
                    }
                } catch (Exception e) {
                    // Не сбрасываем currentMedia при ошибке, оставляем старые данные
                } finally {
                    isUpdating = false;
                }
            }, executor);
        }
        
        // Скрываем только если прошло больше HIDE_DELAY с последнего успешного обновления
        boolean shouldHide = currentMedia == null || 
                           (System.currentTimeMillis() - lastSuccessfulUpdate) > HIDE_DELAY;
        
        // Обновляем анимацию
        if (!shouldHide) {
            this.alpha.update(1.0F);
        } else {
            this.alpha.update(0.0F);
        }

        if (currentMedia == null || this.alpha.getValue() < 0.05F) {
            this.width = 100.0F;
            this.height = 40.0F;
            return;
        }

        Theme theme = Javelin.getInstance().getThemeManager().getCurrentTheme();
        
        String title = currentMedia.getTitle() != null ? currentMedia.getTitle() : "Unknown";
        String artist = currentMedia.getArtist() != null ? currentMedia.getArtist() : "Unknown Artist";
        
        // Обрезаем название до 20 символов с многоточием
        if (title.length() > 25) {
            title = title.substring(0, 25) + "...";
        }
        
        // Вычисляем размеры
        float titleWidth = Fonts.SEMIBOLD.getWidth(title, 8.25F);
        float artistWidth = Fonts.SEMIBOLD.getWidth(artist, 7.5F);
        float maxTextWidth = Math.max(titleWidth, artistWidth);
        
        float componentWidth = maxTextWidth + 30.0F;
        float componentHeight = 45.0F;
        
        this.width = componentWidth;
        this.height = componentHeight;
        
        // Фон компонента
        DrawUtil.drawBlur(ctx.getMatrices(), posX, posY, componentWidth, componentHeight, 0.0F, BorderRadius.all(3.5F),
            new ColorRGBA(0, 0, 0, 255.0F * this.alpha.getValue()));
        DrawUtil.drawRoundedRect(ctx.getMatrices(), posX, posY, componentWidth, componentHeight, BorderRadius.all(3.0F),
            theme.getForegroundColor().darker(0.25F).withAlpha(19.0F * this.alpha.getValue()));
        DrawUtil.drawRoundedBorder(ctx.getMatrices(), posX, posY, componentWidth, componentHeight, 0.5F, BorderRadius.all(3.0F), 
            new ColorRGBA(0, 0, 0, 100.0F * this.alpha.getValue()));
        
        // PNG иконка музыки
        float musicX = -1.2F;
        float musicY = 2F;
        float musicSize = 30F;
        float imgX = posX + musicX;
        float imgY = posY + musicY;
        ctx.drawTexture(Javelin.id("icons/music.png"), imgX, imgY, musicSize, musicSize, 
            theme.getColor().withAlpha(255.0F * this.alpha.getValue()));
        
        // Название трека
        ctx.drawText(Fonts.SEMIBOLD.getFont(7.5F), title, posX + 28.5F, posY + 6F,
            new ColorRGBA(-1).withAlpha(255.0F * this.alpha.getValue()));
        
        // Исполнитель
        ctx.drawText(Fonts.SEMIBOLD.getFont(6.5F), artist, posX + 28.5F, posY + 16.0F,
            new ColorRGBA(166, 166, 166, 255.0F * this.alpha.getValue()));
        
        // Прогресс бар
        if (currentMedia.getDuration() > 0) {
            float progress = (float) currentMedia.getPosition() / (float) currentMedia.getDuration();
            progress = Math.max(0.0F, Math.min(3.0F, progress));
            
            float barW = componentWidth - 7.5F;
            float barH = 6.5F;
            float barXPos = posX + 4F;
            float barYPos = posY + componentHeight - 12.0F;
            
            // Фон прогресс бара
            DrawUtil.drawRoundedRect(ctx.getMatrices(), barXPos, barYPos, barW, barH, BorderRadius.all(1.75F),
                new ColorRGBA(50, 50, 50, 255.0F * this.alpha.getValue()));
            
            // Заполненная часть (не выходит за пределы)
            float filledWidth = Math.min(barW * progress, barW);
            if (filledWidth > 0.5F) {
                DrawUtil.drawRoundedRect(ctx.getMatrices(), barXPos, barYPos, filledWidth, barH, BorderRadius.all(1.75F),
                    theme.getColor().withAlpha(255.0F * this.alpha.getValue()));
            }
        }
    }
}
