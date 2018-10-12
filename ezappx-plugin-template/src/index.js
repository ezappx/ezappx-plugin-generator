import loadComponents from './components';
import loadBlocks from './blocks';

export default (editor, opts = {}) => {
  const options = {
    ...{
      // default options
    }, ...opts
  };

  // Add components
  loadComponents(editor, options);

  // Add blocks
  loadBlocks(editor, options);
};
