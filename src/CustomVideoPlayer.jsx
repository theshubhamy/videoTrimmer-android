import {requireNativeComponent, Platform, View} from 'react-native';
import React, {useEffect} from 'react';


const CustomVideoPlayerAndroid =
  Platform.OS === 'android'
    ? requireNativeComponent('CustomVideoPlayerAndroid')
    : null;

const VideoPlayer = ({videoUrl, paused = false, muted = false, style}) => {
  useEffect(() => {
    // Check URL accessibility
    fetch(videoUrl, {method: 'HEAD'})
      .then(response => {
        if (!response.ok) {
          throw new Error(`HTTP ${response.status}`);
        }
        console.log('[VideoPlayer] URL is accessible');
      })
      .catch(err =>
        console.error('[VideoPlayer] URL check failed:', err, videoUrl),
      );
  }, [videoUrl]);

  const handleError = event => {
    const errorMessage = event.nativeEvent?.error || 'Unknown error occurred';
    console.error('[VideoPlayer] Error:', errorMessage);
  };

  const handleReady = () => {
    console.log('[VideoPlayer] Ready');
  };

  const handleEnd = () => {
    console.log('[VideoPlayer] Playback ended');
  };

  // Common props for both platforms
  const videoProps = {
    style: style,
    paused,
    muted,
  };

  // Platform specific props
  const androidProps = {
    ...videoProps,
    sourceUrl: videoUrl,
    onReady: handleReady,
    onError: handleError,
    onEnd: handleEnd,
  };

 

  return (
    <View>
        <CustomVideoPlayerAndroid {...androidProps} />
    </View>
  );
};

export default VideoPlayer;
