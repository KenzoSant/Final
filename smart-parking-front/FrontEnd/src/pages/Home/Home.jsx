import React from 'react';
import Header from '../../components/Header/Header';
import SearchBar from '../../components/SearchBar/SearchBar';
import BookingInfo from '../../components/BookingInfo/BookingInfo';
import LocationDisplay from '../../components/LocationDisplay/LocationDisplay';
import HistoryPage from '../../components/HistoryPage/HistoryPage';

import './Home.css'

const Home = () => {
  return (
    <div>
      <Header />
      <BookingInfo />
      {/* <SearchBar /> */}
      <HistoryPage isHomePage={true} />
    </div>
  );
};

export default Home;
