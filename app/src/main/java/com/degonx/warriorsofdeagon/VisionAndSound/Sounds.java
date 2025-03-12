package com.degonx.warriorsofdeagon.VisionAndSound;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;

import com.degonx.warriorsofdeagon.Enums.AreaEnums.Areas;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Sounds {

    public static final MediaPlayer mediaPlayer = new MediaPlayer();
    public static MediaPlayer mediaPlayer2;
    private static String playingNow = "";
    public static float vol = 1f;
    static List<String> Songs;
    static int Index = 0;

    public static void playBGM(Context mc, Areas area) throws IOException {
        String bgmName = null;

        //select mp3 file
        if (area != null) {
            String areaType = area.getAreaType();
            if (areaType.equals("mobs_area") || areaType.equals("boss_area"))
                bgmName = "battlebgm.mp3";
            else if (areaType.equals("safe_town"))
                bgmName = "breakbgm.mp3";
        } else
            bgmName = "loginbgm.mp3";

        //check if the same file is not playing already
        if (!playingNow.equals(bgmName)) {
            //stop if not the same file
            if (mediaPlayer.isPlaying())
                mediaPlayer.stop();
            AssetFileDescriptor afd = mc.getAssets().openFd(bgmName);
            try {
                //set file to be played
                mediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build());
                mediaPlayer.reset();
                Objects.requireNonNull(mediaPlayer).setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                afd.close();
                try {
                    //stats playing file
                    playingNow = bgmName;
                    mediaPlayer.prepare();
                    muteBGM(vol);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                } catch (IllegalStateException ignored) {
                }
            } catch (IllegalArgumentException | IllegalStateException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    //mute or unmute BGM
    public static void muteBGM(float newVol) {
        mediaPlayer.setVolume(newVol, newVol);
        vol = newVol;
    }

    //play music from folder
    public static void playMusic(Context mc) {
        //load mediaplayer2 and get songs from the folder into Songs list
        if (mediaPlayer2 == null) {
            mediaPlayer2 = new MediaPlayer();
            Songs = new ArrayList<>();

            //check if folder exist
            File dir = new File(Environment.getExternalStorageDirectory() + "/WOD Media");
            if (dir.exists()) {

                //get all files in the folder
                File[] fileList = dir.listFiles();
                if (fileList != null) {

                    //add them to Songs list if files are MP3
                    for (File file : fileList)
                        if (file.getName().endsWith("mp3"))
                            Songs.add(file.getName());
                }
            }
        }

        if (Songs.size() > 0) {
            try {
                //set and start mediaplayer2
                mediaPlayer2.reset();
                mediaPlayer2.setDataSource(mc, Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/WOD Media/" + Songs.get(Index))));
                mediaPlayer2.prepare();
                mediaPlayer2.setVolume(5, 5);
                mediaPlayer2.start();
                mediaPlayer2.setOnCompletionListener(c -> nextSong(mc));

                //notify if BGM is playing
                if (vol > 0)
                    Toasts.makeToast(mc, "BGM is playing, recommended to mute it");
            } catch (IllegalStateException | IOException ignored) {
            }
        } else
            Toasts.makeToast(mc, "WOD Media folder is empty or not exist!");
    }

    //mediaplayer2 next song
    public static void nextSong(Context mc) {
        if (++Index >= Songs.size())
            Index = 0;
        playMusic(mc);
    }

    //play or pause mediaplayer2
    public static void playPauseMusic() {
        if (mediaPlayer2.isPlaying())
            mediaPlayer2.pause();
        else
            mediaPlayer2.start();
    }
}
