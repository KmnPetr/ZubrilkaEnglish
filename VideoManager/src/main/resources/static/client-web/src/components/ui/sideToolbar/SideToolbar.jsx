import React from "react";
import "./sideToolbar.css";

/**
 * для показа панели инструментов сверху или снизу от карточки слова или фразы
 * необходимо чтобы элемент родитель имел стиль "position: relative"
 */
const SideToolbar = ({ isShow = true, position = "top", background='#575757', z_index=999, children }) => {
  if (!isShow) return null;

  const positionClass = position === "top" ? "toolbar_top" : "toolbar_bottom";

  return (
    <div className={`side_toolbar ${positionClass}`} style={{background:background,zIndex:z_index}}>
      {children}
    </div>
  );
};

export default SideToolbar;
