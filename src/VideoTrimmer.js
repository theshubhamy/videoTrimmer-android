import {NativeModules} from 'react-native';

const {VideoTrimmer} = NativeModules;

export const openVideoTrimmer = async videoUri => {
  console.log('Opening video trimmer for:', videoUri);
  try {
    const trimmedVideoUri = await VideoTrimmer.openTrimView(videoUri.uri);
    console.log('Trimmed video saved at:', trimmedVideoUri);
  } catch (error) {
    console.error('Error trimming video:', error);
  }
};
